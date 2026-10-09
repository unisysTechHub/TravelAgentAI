package com.unisystechhub.ai.tools;

import com.unisystechhub.ai.model.Contact;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.cache.interceptor.CacheableOperation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContactsTools {
private final JdbcTemplate jdbcTemplate;

@Tool(description = "Find contacts in a given city")

List<Contact> findContactByCity(String city){
    String sql = "SELECT * from contacts where city = ?";
    RowMapper<Contact> rowMapper = (rs,rowNumber) -> new Contact(
          rs.getInt("id") , rs.getString("name"),rs.getString("email"),rs.getString("city"));
    return jdbcTemplate.query(sql,rowMapper,city);
}
@Tool( description = "formats list of contacts into CSV wth Headers:Name,Email,City")
public  String formatASCSV(List<Contact> contacts){
    StringBuilder stringBuilder = new StringBuilder("Name,Email,City\n");
    for (Contact contact : contacts){
        stringBuilder.append(contact.name()).append(',')
                .append(contact.email()).append(',')
                .append(contact.city()).append('\n');
    }
return  stringBuilder.toString();
}

}
