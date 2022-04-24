package oogasalad.com.stripe;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import spark.Spark;

import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

public class StripeIntegration {

  public StripeIntegration() {
    port(4242);
  }

  public void purchaseItem() throws URISyntaxException, IOException, InterruptedException {
    makeRequest();
    openWebPage();

  }

  private void makeRequest() throws InterruptedException {

    // This is your test secret API key.
    Stripe.apiKey = "sk_test_51KpiXJCOTY4jZDr4HsPOnix8e9JuuToD27JhxIPCUSlXfPogIW3n05G0BxcWAKAipD2Dq"
        + "dBdS6qYhV0XkvfOKhcW00m197JWbn";
    Spark.stop();
    Thread.sleep(500);
    port(4242);
    staticFiles.externalLocation(
        Paths.get("public").toAbsolutePath().toString());
    post("/create-checkout-session", (request, response) -> {
      String YOUR_DOMAIN = "http://localhost:4242";
      SessionCreateParams params =
          SessionCreateParams.builder()
              .setMode(SessionCreateParams.Mode.PAYMENT)
              .setSuccessUrl(YOUR_DOMAIN + "/success.html")
              .setCancelUrl(YOUR_DOMAIN + "/cancel.html")
              .addLineItem(
                  SessionCreateParams.LineItem.builder()
                      .setQuantity(1L)
                      .setPrice("price_1Kpil7COTY4jZDr4r0YEy78K")
                      .build())
              .build();
      Session session = Session.create(params);
      response.redirect(session.getUrl(), 303);
      return "";
    });
  }

  private void openWebPage() throws URISyntaxException, IOException {
    Desktop.getDesktop().browse(new URI("http://localhost:4242/checkout.html"));
  }

}
