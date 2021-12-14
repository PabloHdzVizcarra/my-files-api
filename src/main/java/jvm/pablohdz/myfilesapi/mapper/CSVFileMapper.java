package jvm.pablohdz.myfilesapi.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import jvm.pablohdz.myfilesapi.dto.CSVFileDto;
import jvm.pablohdz.myfilesapi.model.MyFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CSVFileMapper {

  @Mapping(target = "createdAt", expression = "java(dateISOFormat(file.getCreatedAt()))")
  CSVFileDto myFileToCSVFileDto(MyFile file);

  default String dateISOFormat(Long date) {
    TimeZone timeZone = TimeZone.getDefault();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    dateFormat.setTimeZone(timeZone);
    return dateFormat.format(new Date(date));
  }
}
