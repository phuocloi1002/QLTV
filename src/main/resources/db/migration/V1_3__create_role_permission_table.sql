
CREATE TABLE role_permission (
                                 role_id CHAR(36) NOT NULL,
                                 permission_id CHAR(36) NOT NULL,
                                 PRIMARY KEY (role_id, permission_id),
                                 CONSTRAINT fk_role
                                     FOREIGN KEY (role_id) REFERENCES role(id),
                                 CONSTRAINT fk_permission
                                     FOREIGN KEY (permission_id) REFERENCES permission(id)
);
