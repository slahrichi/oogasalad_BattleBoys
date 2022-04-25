package oogasalad.com.stripe;

import com.stripe.exception.StripeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import spark.Spark;
import util.DukeApplicationTest;

public class StripeIntegrationTest extends DukeApplicationTest {

  private StripeIntegration integration;
  private static final String HTML_PAGE = "<!DOCTYPE html><!-- HTML code provided by Stripe for "
      + "checkout site --><html>  <head>    <title>Buy cool new product</title>    "
      + "<link rel=\"stylesheet\" href=\"style.css\">    "
      + "<script src=\"https://polyfill.io/v3/polyfill.min.js?version=3.52.1&features=fetch\"></script>    "
      + "<script src=\"https://js.stripe.com/v3/\"></script>  </head>  <body>      "
      + "<form action=\"/create-checkout-session\" method=\"POST\">        "
      + "<button type=\"submit\" id=\"checkout-button\">Checkout</button>      "
      + "</form>  </body></html>";

  @BeforeEach
  void setup() {
    integration = new StripeIntegration();
  }

  @Test
  void testStripeBasic() throws URISyntaxException, IOException, InterruptedException {
    integration.purchaseItem("Basic Shot");
    Thread.sleep(2000);
    assertEquals(HTML_PAGE, readReferenceFromWeb(new URL("http://localhost:4242/checkout.html")));
    Spark.stop();
  }

  @Test
  void testPaymentDenial()
      throws URISyntaxException, IOException, InterruptedException, StripeException {
    integration.purchaseItem("Basic Shot");
    assertEquals(false, integration.hasBeenPaid());
    Spark.stop();
  }


  public static String readReferenceFromWeb(URL url) {
    BufferedReader reader;
    try {
      reader = new BufferedReader(new InputStreamReader(url.openStream()));
    } catch (IOException e) {
      throw new IllegalArgumentException();
    }
    return scan(reader);
  }


  private static String scan(BufferedReader reader) {
    //method to compile text from a BufferedReader into a String
    StringBuilder reference = new StringBuilder();
    try {
      String current;
      while ((current = reader.readLine()) != null) {
        reference.append(current);
      }
    } catch (IOException ex) {
      throw new IllegalArgumentException();
    }
    return reference.toString();
  }



}
