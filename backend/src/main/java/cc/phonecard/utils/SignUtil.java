package cc.phonecard.utils;

import io.vertx.core.http.HttpMethod;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Formatter;
import java.util.Objects;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;

public class SignUtil {
  private static final String HMAC_SHA256 = "HmacSHA256";

  public static void main(String args[]) throws Exception {
    String expectedSignature = "f0e88d33f4821fb74d605a68d9c12fe78366c30d21066beee2585a50d36f4acf";
    final String SECRET = "LQpiL#jiSSvu%s9QZN4k6DbpSF%&P$f#";
    String signature = generateSignature(
      SECRET,
      HttpMethod.POST.toString(),
      "http://demo.example.com/webhook",
      1563276169752l,
      "{\"a\":1}"
    );
    System.out.println(signature.equals(expectedSignature));
    signature = generateSignature(
      SECRET,
      HttpMethod.GET.toString(),
      "http://demo.example.com/webhook?a=1",
      1563276169752l,
      null
    );
    System.out.println(signature);
    System.out.println(signature.equals(expectedSignature));
  }

  private static String toHexString(byte[] bytes) {
    Formatter formatter = new Formatter();
    for (byte b : bytes) {
      formatter.format("%02x", b);
    }
    return formatter.toString();
  }

  public static String calculateHMACSHA256(String data, String key) throws Exception {
    SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
    Mac mac = Mac.getInstance(HMAC_SHA256);

    mac.init(secretKeySpec);

    return toHexString(mac.doFinal(data.getBytes()));
  }

  private static String generateSignature(String secret, String method, String uri, long timestamp)
    throws Exception {
    return generateSignature(secret, method, uri, timestamp, "");
  }

  public static String generateSignature(String secret, String method, String uri, long timestamp,
    String bodyString) throws Exception {
    String methodUpperCase = method.toUpperCase();
    URI url = new URI(uri);
    String path = url.getPath();
    String query = url.getQuery();

    if (HttpMethod.GET.toString().equals(method) && StringUtils.isNotBlank(query)) {
      path = path + "?" + query;
    }

    String signatureData = methodUpperCase + path + timestamp;

    if (Objects.nonNull(bodyString)) {
      signatureData += bodyString;
    }

    return calculateHMACSHA256(signatureData, secret);
  }

}
