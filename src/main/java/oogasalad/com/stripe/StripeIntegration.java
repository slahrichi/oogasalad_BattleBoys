package oogasalad.com.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import spark.Spark;

import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

public class StripeIntegration {

  private ResourceBundle resources = ResourceBundle.getBundle("/stripeItemIds");
  private Session session;

  private static final String PAID = "paid";
  private static final String API_KEY = "sk_test_51KpiXJCOTY4jZDr4HsPOnix8e9JuuToD27JhxIPCUSlXfPogI"
      + "W3n05G0BxcWAKAipD2DqdBdS6qYhV0XkvfOKhcW00m197JWbn";
  private static final String MY_DOMAIN = "http://localhost:4242";
  private static final String SUCCESS = "/success.html";
  private static final String CANCEL = "/cancel.html";
  private static final String CHECKOUT = "/checkout.html";
  private static final String CHECKOUT_SESSION = "/create-checkout-session";
  private static final String PUBLIC = "public";
  private static final int PORT = 4242;
  private static final int WAIT = 500;
  private static final int CODE = 303;

  public StripeIntegration() {
    port(PORT);
  }

  public void purchaseItem(String item) throws URISyntaxException, IOException, InterruptedException {
    makeRequest(item);
    openWebPage();
  }

  private String getAPIKeyFromItem(String item) {
    String key = String.join("", item.split(" "));
    return resources.getString(key);
  }
  private void makeRequest(String item) throws InterruptedException {
    Stripe.apiKey = API_KEY;
    Spark.stop();
    Thread.sleep(WAIT);
    port(PORT);
    staticFiles.externalLocation(
        Paths.get(PUBLIC).toAbsolutePath().toString());
    post(CHECKOUT_SESSION, (request, response) -> {
      SessionCreateParams params =
          SessionCreateParams.builder()
              .setMode(SessionCreateParams.Mode.PAYMENT)
              .setSuccessUrl(MY_DOMAIN + SUCCESS)
              .setCancelUrl(MY_DOMAIN + CANCEL)
              .addLineItem(
                  SessionCreateParams.LineItem.builder()
                      .setQuantity(1L)
                      .setPrice(getAPIKeyFromItem(item))
                      .build())
              .build();
      session = Session.create(params);
      response.redirect(session.getUrl(), CODE);
      return "";
    });
  }

  public boolean hasBeenPaid() throws StripeException {
    if (session == null) {
      return false;
    }
    Session updatedSession = Session.retrieve(session.getId());
    return updatedSession.getPaymentStatus().equals(PAID);

  }

  private void openWebPage() throws URISyntaxException, IOException {
    Desktop.getDesktop().browse(new URI(MY_DOMAIN + CHECKOUT));
  }

}
