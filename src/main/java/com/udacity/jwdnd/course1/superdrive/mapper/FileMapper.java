package com.udacity.jwdnd.course1.superdrive.mapper;

import com.udacity.jwdnd.course1.superdrive.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM files WHERE fileid = #{fileId}")
    File getFileById(int fileId);
    
    @Select("SELECT * FROM files WHERE filename = #{fileName} AND userid = #{userId}")
    File getFileByName(String fileName, int userId);
    
    @Select("SELECT * FROM files WHERE userid = #{userId}")
    List<File> getAllFiles(int userId);
    
    @Insert("INSERT INTO files(filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int addFile(File file);
    
    @Update("UPDATE files SET filename = #{fileName}, filesize = #{fileSize}, filedata = {fileData} " +
            "WHERE fileid = #{fileId}")
    int updateFile(File file);
    
    @Delete("DELETE FROM files WHERE fileid = #{fileId}")
    int deleteFile(int fileId);
}
