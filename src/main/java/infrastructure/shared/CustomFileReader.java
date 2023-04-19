package infrastructure.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class CustomFileReader {

  public List<String> readFile(String fileName) throws IOException {
    List<String> result = new ArrayList<>();
    try (InputStream is = new ClassPathResource(fileName).getInputStream();
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr)) {

      br.lines().forEach(result::add);
    }
    return result;
  }
}
