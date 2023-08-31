// /*
//  * This Java source file was generated by the Gradle 'init' task.
//  */
package dowjones_data;

// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
// import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class AppTest {
    private App app;
    private CloseableHttpClient httpClient;
    private CloseableHttpResponse httpResponse;
    private Method fetchStockPriceMethod;

    @Before
    public void setUp() throws Exception {
        app = new App();
        httpClient = mock(CloseableHttpClient.class);
        httpResponse = mock(CloseableHttpResponse.class);

        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);

        // Get the fetchStockPrice method using reflection
        fetchStockPriceMethod = App.class.getDeclaredMethod("fetchStockPrice");
        fetchStockPriceMethod.setAccessible(true);
    }

    @Test
    public void testFetchStockPrice() throws Exception {
        String responseJson = "{\"ask\": 187.48}";
        InputStream responseStream = new ByteArrayInputStream(responseJson.getBytes());

        when(httpResponse.getEntity().getContent()).thenReturn(responseStream);

        // Call the private method using reflection
        double actualPrice = (double) fetchStockPriceMethod.invoke(app);
        assertEquals(187.48, actualPrice, 0.001); // Check if the fetched price matches the expected value
    }
}