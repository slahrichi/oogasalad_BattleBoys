package oogasalad.stripe;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

public class StripeIntegration {

  public StripeIntegration() throws URISyntaxException, IOException {
    buildServer();
    openWebPage();
  }

  private void buildServer() {
    port(4242);

    // This is your test secret API key.
    Stripe.apiKey = "sk_test_51KpiXJCOTY4jZDr4HsPOnix8e9JuuToD27JhxIPCUSlXfPogIW3n05G0BxcWAKAipD2Dq"
        + "dBdS6qYhV0XkvfOKhcW00m197JWbn";

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
      System.out.println(session.getUrl());
      response.redirect(session.getUrl(), 303);
      System.out.println(session.getUrl());
      return "";
    });
  }

  private void openWebPage() throws URISyntaxException, IOException {
    Desktop.getDesktop().browse(new URI("http://localhost:4242/checkout.html"));
  }

}
