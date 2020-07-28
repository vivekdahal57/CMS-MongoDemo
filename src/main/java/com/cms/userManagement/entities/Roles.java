package com.cms.userManagement.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Created by i82325 on 7/3/2020.
 */
@Document(collection = "roles")
public class Roles {
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    @Id
    public BigInteger _id;

    public String roleName;

    public BigInteger get_id() {
        return _id;
    }

    public void set_id(BigInteger _id) {
        this._id = _id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
