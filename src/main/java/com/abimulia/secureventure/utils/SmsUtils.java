/**
 * SmsUtils.java
 * 27-Nov-2024
 */
package com.abimulia.secureventure.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import static com.twilio.rest.api.v2010.account.Message.creator;

/**
* 
* @author abimu
*
* @version 1.0 (27-Nov-2024)
* @since 27-Nov-2024 12:14:18 AM
* 
* 
* Copyright(c) 2024 Abi Mulia
*/
public class SmsUtils {
	public static final String FROM_NUMBER = "<Your own number from Twilio>";
    public static final String SID_KEY = "<Your own key>";
    public static final String TOKEN_KEY = "<Your own key>";

    public static void sendSMS(String to, String messageBody) {
        Twilio.init(SID_KEY, TOKEN_KEY);
        Message message = creator(new PhoneNumber("+" + to), new PhoneNumber(FROM_NUMBER), messageBody).create();
        System.out.println(message);
    }
}
