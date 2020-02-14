package com.java.weddingplz;

public class StringTester {

	public static void main(String[] args) {
		String line2 = "                                                <a href=\"javascript:void(0);\" onClick =\"javascript:smsMeForm('vendorid=12423&vendorName=Dr. Shobha Jindal&vendorAddress=C-5, Green Park Extension, New Delhi–110016&vendorMno=9350260389&vendorEmail=shobhajain1@yahoo.co.in&category_id=37&share_vendor=0');\" title=\"sms\" alt =\"SMS\" class=\"enqury\">";
		String myAddress = line2.split("vendorAddress=")[1].split("&vendorMno")[0];

		String ad2 = "E44, Greater Kailash Part-2, New Delhi-110 048";

		String myPincode = extractPincode(ad2);
		System.out.println(myPincode);
	}

	private static String extractPincode(String address) {
		char wChar = '–';
		char hyp = '-';
		int lastIndex;
		if(address.lastIndexOf(wChar) == -1) {
			lastIndex = address.lastIndexOf(hyp);
		} else {
			lastIndex = address.lastIndexOf(wChar);
		}
		String myPincode = address.substring(lastIndex + 1);
		myPincode = myPincode.replace(" ", "");
		return myPincode;
	}
}
