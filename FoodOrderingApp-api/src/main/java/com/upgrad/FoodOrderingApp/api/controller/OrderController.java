package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import com.upgrad.FoodOrderingApp.api.common.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByName(@RequestHeader("authorization") final String authorization,
            @PathVariable("coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        String accessToken = Utility.getTokenFromAuthorization(authorization);

        customerService.getCustomer(accessToken);

        CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);

        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
        couponDetailsResponse.setPercent(couponEntity.getPercent());
        couponDetailsResponse.setId(UUID.fromString(couponEntity.getUuid()));
        couponDetailsResponse.setCouponName(couponEntity.getCouponName());

        return new ResponseEntity<>(couponDetailsResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getPastOrdersByUser(@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {

        String accessToken = Utility.getTokenFromAuthorization(authorization);

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        List<OrderEntity> orderentity = orderService.getPastOrdersByUser(customerEntity.getUuid());

        List<OrderList> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderentity) {
            OrderList order = new OrderList();
            order.setId(UUID.fromString(orderEntity.getUuid()));
            order.setDate(orderEntity.getDate().toString());
            order.setDiscount(new BigDecimal(orderEntity.getDiscount()));
            order.setCustomer(getOrderListCustomer(orderEntity.getCustomer()));
            order.setCoupon(getOrderListCoupon(orderEntity.getCoupon()));
            order.setAddress(getOrderListAddress(orderEntity.getAddress()));
            order.setPayment(getOrderListPayment(orderEntity.getPayment()));
            order.setBill(new BigDecimal(orderEntity.getBill()));
            List<OrderItemEntity> orderItems = orderEntity.getOrderItems();
            order.setItemQuantities(getItemQuantityResponseList(orderItems));
            orders.add(order);
        }

        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();
        customerOrderResponse.setOrders(orders);
        return new ResponseEntity<>(customerOrderResponse, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader("authorization") final String authorization,
            @RequestBody(required = true) SaveOrderRequest saveOrderRequest)
            throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException,
            PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
        String accessToken = Utility.getTokenFromAuthorization(authorization);


        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        PaymentEntity paymentEntity =
                paymentService.getPaymentByUUID(saveOrderRequest.getPaymentId().toString());

        CouponEntity couponEntity =
                orderService.getCouponByCouponId(saveOrderRequest.getCouponId().toString());

        AddressEntity addressEntity =
                addressService.getAddressByUUID(saveOrderRequest.getAddressId(), customerEntity);

        RestaurantEntity restaurantEntity =
                restaurantService.restaurantByUUID(saveOrderRequest.getRestaurantId().toString());

        List<OrderItemEntity> orderItemEntities = new ArrayList<>();

        List<ItemQuantity> itemQuantities = saveOrderRequest.getItemQuantities();
        for (ItemQuantity itemQuantity : itemQuantities) {
            ItemEntity itemEntity = itemService.getItemByUUID(itemQuantity.getItemId().toString());
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setPrice(itemQuantity.getPrice());
            orderItem.setQuantity(itemQuantity.getQuantity());
            orderItem.setItem(itemEntity);
            orderItemEntities.add(orderItem);
        }

        OrderEntity order = new OrderEntity();
        order.setUuid(UUID.randomUUID().toString());
        order.setBill(saveOrderRequest.getBill().doubleValue());
        order.setDiscount(saveOrderRequest.getDiscount().doubleValue());
        order.setAddress(addressEntity);
        order.setCoupon(couponEntity);
        order.setPayment(paymentEntity);
        order.setRestaurant(restaurantEntity);
        order.setDate(ZonedDateTime.now());
        order.setCustomer(customerEntity);
        order.setOrderItems(Collections.emptyList());
        order = orderService.saveOrder(order);
        if (order != null) {
            for (OrderItemEntity orderItemEntity : orderItemEntities) {
                orderItemEntity.setOrder(order);
                orderService.saveOrderItem(orderItemEntity);
            }
        }
        SaveOrderResponse saveOrderResponse = new SaveOrderResponse();
        saveOrderResponse.setId(order.getUuid().toString());
        saveOrderResponse.setStatus("ORDER SUCCESSFULLY PLACED");

        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.CREATED);
    }


    private OrderListCustomer getOrderListCustomer(CustomerEntity customer) {
        OrderListCustomer orderListCustomer = new OrderListCustomer();
        orderListCustomer.setId(UUID.fromString(customer.getUuid()));
        orderListCustomer.setFirstName(customer.getFirstName());
        orderListCustomer.setEmailAddress(customer.getEmail());
        orderListCustomer.setContactNumber(customer.getContactNumber());
        orderListCustomer.setLastName(customer.getLastName());
        return orderListCustomer;
    }

    private OrderListPayment getOrderListPayment(PaymentEntity payment) {
        OrderListPayment orderListPayment = new OrderListPayment();
        orderListPayment.setId(UUID.fromString(payment.getUuid()));
        orderListPayment.setPaymentName(payment.getPaymentName());
        return orderListPayment;
    }

    private OrderListAddress getOrderListAddress(AddressEntity address) {
        OrderListAddress orderListAddress = new OrderListAddress();
        orderListAddress.setId(UUID.fromString(address.getUuid()));
        orderListAddress.setLocality(address.getLocality());
        orderListAddress.setCity(address.getCity());
        orderListAddress.setPincode(address.getPincode());
        orderListAddress.setFlatBuildingName(address.getFlatBuilNo());
        OrderListAddressState orderListAddressState = new OrderListAddressState();
        orderListAddressState.setId(UUID.fromString(address.getState().getUuid()));
        orderListAddressState.setStateName(address.getState().getStateName());
        orderListAddress.setState(orderListAddressState);
        return orderListAddress;
    }
    private OrderListCoupon getOrderListCoupon(CouponEntity coupon) {
        OrderListCoupon orderListCoupon = new OrderListCoupon();
        orderListCoupon.setId(UUID.fromString(coupon.getUuid()));
        orderListCoupon.setCouponName(coupon.getCouponName());
        orderListCoupon.setPercent(coupon.getPercent());
        return orderListCoupon;
    }



    private List<ItemQuantityResponse> getItemQuantityResponseList(List<OrderItemEntity> items) {
        List<ItemQuantityResponse> responseList = new ArrayList<>();

        for (OrderItemEntity orderItem : items) {
            ItemQuantityResponse response = new ItemQuantityResponse();

            ItemQuantityResponseItem responseItem = new ItemQuantityResponseItem();
            responseItem.setId(UUID.fromString(orderItem.getItem().getUuid()));
            responseItem.setItemName(orderItem.getItem().getItemName());
            responseItem.setItemPrice(orderItem.getItem().getPrice());
            ItemQuantityResponseItem.TypeEnum itemType =
                    Integer.valueOf(orderItem.getItem().getType()) == 0
                            ? ItemQuantityResponseItem.TypeEnum.VEG
                            : ItemQuantityResponseItem.TypeEnum.NON_VEG;
            responseItem.setType(itemType);
            response.setItem(responseItem);

            response.setQuantity(orderItem.getQuantity());
            response.setPrice(orderItem.getPrice());
            responseList.add(response);
        }
        return responseList;
    }
}
