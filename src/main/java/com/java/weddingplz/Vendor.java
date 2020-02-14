package com.java.weddingplz;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class Vendor {

    String  name;
    String  address;
    String  phoneNumber;
    String  email;
    Integer pinCode;

    String profile() {
        StringBuilder profileBuilder = new StringBuilder();
        profileBuilder.append(name).append(System.getProperty("line.separator")).append("Address: ").append(address).append(System.getProperty("line.separator")).append("Phone: ").append(phoneNumber);
        return profileBuilder.toString();
    }

    public static void main(String[] args) {
        Vendor raja = Vendor.builder().phoneNumber("33").address("900 S. Bishop St").name("Raja").build();
        String profile = raja.profile();
        System.out.println();

    }

}
