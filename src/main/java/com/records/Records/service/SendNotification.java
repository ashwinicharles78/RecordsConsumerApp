package com.records.Records.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.records.Records.config.TwilioConfig;
import com.records.Records.model.KafkaUserData;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Objects;

@Service
public class SendNotification {

    @Autowired
    private TwilioConfig twilioConfig;

    @Autowired
    private Environment env;

    @Autowired
    private TemplateEngine templateEngine;

    // Replace sender@example.com with your "From" address.
    // This address must be verified with Amazon SES.
    static final String FROM = "mail@ashwinicharles.info";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified.
    static final String TO = "ashwinicharles78@gmail.com";

    // The configuration set to use for this email. If you do not want to use a
    // configuration set, comment the following variable and the
    // .withConfigurationSetName(CONFIGSET); argument below.
    static final String CONFIGSET = "ConfigSet";

    // The subject line for the email.
    static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

    // The email body for recipients with non-HTML email clients.
    static final String TEXTBODY = "This email was sent through Amazon SES "
            + "using the AWS SDK for Java.";
    private static final String SPACE = " ";


    public void sendNotification(KafkaUserData userData) {
        PhoneNumber to = new PhoneNumber(userData.getPhoneNumber());
        PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());

        String otpMessage = "Dear " +  userData.getFirstName() + ", you are now successfully registered with the Records Application.";
        Message
                .creator(to, from,
                        otpMessage)
                .create();
        this.sendEmail(userData);
    }

    private void sendEmail(KafkaUserData userData) {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(
                Objects.requireNonNull(env.getProperty("aws.accessKeyId")),
                Objects.requireNonNull(env.getProperty("aws.administrativeKey")),
                "654654602872"
        );

        try {
            String processedString = "";
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.US_EAST_1).build();
            if(!userData.getEmail().isEmpty()) {
                Context context = new Context();
            /*
            content is the variable defined in our HTML template within the div tag
            */
                context.setVariable("email", userData.getEmail());
                context.setVariable("name", userData.getFirstName()+SPACE+userData.getLastName());
                processedString = templateEngine.process("template", context);
            }
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(TO))
                    .withMessage(new com.amazonaws.services.simpleemail.model.Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(processedString))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(TEXTBODY)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(SUBJECT)))
                    .withSource(FROM);
            // Comment or remove the next line if you are not using a
            // configuration set
//                    .withConfigurationSetName(CONFIGSET);
            client.sendEmail(request);

        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
        }

    }
}
