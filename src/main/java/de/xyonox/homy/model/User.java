package de.xyonox.homy.model;

// build.gradle/maven: include ORMLite, lombok dependencies

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DatabaseTable(tableName = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true, canBeNull = false)
    private String username;

    @DatabaseField(unique = true, canBeNull = false)
    private String email;

    @DatabaseField(canBeNull = false)
    private String passwordHash;

    @DatabaseField
    private String role; // e.g. "USER","ADMIN"

    @DatabaseField
    private long createdAt;
}