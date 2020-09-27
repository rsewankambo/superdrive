package com.udacity.jwdnd.course1.superdrive.mapper;

import com.udacity.jwdnd.course1.superdrive.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM credentials WHERE credentialid = #{credentialId}")
    Credential getCredential(int credentialId);
    
    @Select("SELECT * FROM credentials WHERE userId = #{userId}")
    List<Credential> getAllCredentials(int userId);
    
    @Insert("INSERT INTO credentials(url, username, key, password, userid) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int addCredential(Credential credential);
    
    @Update("UPDATE credentials " +
            "SET url = #{url}, username = #{username}, key = #{key}, password = #{password}, userid = #{userId} " +
            "WHERE credentialid = #{credentialId}")
    int updateCredential(Credential credential);
    
    @Delete("DELETE FROM credentials WHERE credentialid = #{credentialId}")
    int deleteCredential(int credentialId);
}
