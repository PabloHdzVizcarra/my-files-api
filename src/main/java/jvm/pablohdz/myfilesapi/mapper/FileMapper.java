package jvm.pablohdz.myfilesapi.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import jvm.pablohdz.myfilesapi.dto.CSVFileDataDto;
import jvm.pablohdz.myfilesapi.dto.FileDto;
import jvm.pablohdz.myfilesapi.model.MyFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

  @Mapping(target = "createdAt", expression = "java(dateISOFormat(file.getCreatedAt()))")
  FileDto myFileToCSVFileDto(MyFile file);

  @Mapping(target = "createdAt", expression = "java(dateISOFormat(file.getCreatedAt()))")
  @Mapping(target = "updateAt", expression = "java(dateISOFormat(file.getUpdateAt()))")
  FileDto toCSVFileDto(MyFile file);

  @Mapping(target = "filename", source = "filename")
  @Mapping(target = "contentType", source = "contentType")
  @Mapping(target = "data", source = "bytes")
  CSVFileDataDto toCSVFileDataDto(String filename, String contentType, byte[] bytes);

  default String dateISOFormat(Long date) {
    TimeZone timeZone = TimeZone.getDefault();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    dateFormat.setTimeZone(timeZone);
    return dateFormat.format(new Date(date));
  }
}
