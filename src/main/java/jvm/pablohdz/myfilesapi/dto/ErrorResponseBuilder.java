package jvm.pablohdz.myfilesapi.dto;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ErrorResponseBuilder {
  private String type;
  private String code;
  private String message;
  private List<Map<String, List<String>>> param;
  private String timestamp;
}
