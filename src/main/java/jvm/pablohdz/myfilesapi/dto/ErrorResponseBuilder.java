package jvm.pablohdz.myfilesapi.dto;

import java.util.List;
import java.util.Map;
import jvm.pablohdz.myfilesapi.api.ErrorCode;
import jvm.pablohdz.myfilesapi.api.ErrorType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ErrorResponseBuilder {
  private ErrorType type;
  private ErrorCode code;
  private String message;
  private Map<String, List<String>> param;
  private String timestamp;
}
