

CREATE TABLE book
(
    id             CHAR(36)     NOT NULL,
    created_at     datetime     NULL,
    updated_at     datetime     NULL,
    created_by     VARCHAR(36)  NULL,
    updated_by     VARCHAR(36)  NULL,
    is_deleted     BIT(1)       NULL,
    title          VARCHAR(255) NULL,
    author         VARCHAR(255) NULL,
    isbn           VARCHAR(255) NULL,
    category       VARCHAR(255) NULL,
    publisher      VARCHAR(255) NULL,
    published_year VARCHAR(255) NULL,
    price          DOUBLE       NULL,
    shelf_code     VARCHAR(255) NULL,
    CONSTRAINT pk_book PRIMARY KEY (id)
);

CREATE TABLE book_copy
(
    id                 CHAR(36)     NOT NULL,
    created_at         datetime     NULL,
    updated_at         datetime     NULL,
    created_by         VARCHAR(36)  NULL,
    updated_by         VARCHAR(36)  NULL,
    is_deleted         BIT(1)       NULL,
    barcode            VARCHAR(255) NULL,
    circulation_status VARCHAR(255) NULL,
    condition_status   VARCHAR(255) NULL,
    book_id            CHAR(36)     NOT NULL,
    CONSTRAINT pk_book_copy PRIMARY KEY (id)
);

CREATE TABLE fine
(
    id         CHAR(36)     NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    created_by VARCHAR(36)  NULL,
    updated_by VARCHAR(36)  NULL,
    is_deleted BIT(1)       NULL,
    student_id CHAR(36)     NOT NULL,
    loan_id    CHAR(36)     NULL,
    fine_type  VARCHAR(255) NULL,
    amount     DOUBLE       NULL,
    status     VARCHAR(255) NULL,
    CONSTRAINT pk_fine PRIMARY KEY (id)
);

CREATE TABLE fine_payment
(
    id             CHAR(36)     NOT NULL,
    created_at     datetime     NULL,
    updated_at     datetime     NULL,
    created_by     VARCHAR(36)  NULL,
    updated_by     VARCHAR(36)  NULL,
    is_deleted     BIT(1)       NULL,
    amount         DOUBLE       NULL,
    payment_method VARCHAR(255) NULL,
    paid_at        datetime     NULL,
    fine_id        CHAR(36)     NOT NULL,
    CONSTRAINT pk_finepayment PRIMARY KEY (id)
);

CREATE TABLE incident
(
    id            CHAR(36)     NOT NULL,
    created_at    datetime     NULL,
    updated_at    datetime     NULL,
    created_by    VARCHAR(36)  NULL,
    updated_by    VARCHAR(36)  NULL,
    is_deleted    BIT(1)       NULL,
    title         VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    priority      VARCHAR(255) NULL,
    status        VARCHAR(255) NULL,
    CONSTRAINT pk_incident PRIMARY KEY (id)
);

CREATE TABLE loan
(
    id           CHAR(36)     NOT NULL,
    created_at   datetime     NULL,
    updated_at   datetime     NULL,
    created_by   VARCHAR(36)  NULL,
    updated_by   VARCHAR(36)  NULL,
    is_deleted   BIT(1)       NULL,
    borrowed_at  datetime     NULL,
    due_date     datetime     NULL,
    returned_at  datetime     NULL,
    status       VARCHAR(255) NULL,
    student_id   CHAR(36)     NOT NULL,
    book_copy_id CHAR(36)     NOT NULL,
    CONSTRAINT pk_loan PRIMARY KEY (id)
);

CREATE TABLE notification
(
    id         CHAR(36)     NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    created_by VARCHAR(36)  NULL,
    updated_by VARCHAR(36)  NULL,
    is_deleted BIT(1)       NULL,
    title      VARCHAR(255) NULL,
    content    VARCHAR(255) NULL,
    type       VARCHAR(255) NULL,
    is_read    BIT(1)       NULL,
    user_id    CHAR(36)     NOT NULL,
    CONSTRAINT pk_notification PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id           CHAR(36)     NOT NULL,
    created_at   datetime     NULL,
    updated_at   datetime     NULL,
    created_by   VARCHAR(36)  NULL,
    updated_by   VARCHAR(36)  NULL,
    is_deleted   BIT(1)       NULL,
    reserved_at  datetime     NULL,
    expired_at   datetime     NULL,
    status       VARCHAR(255) NULL,
    student_id   CHAR(36)     NULL,
    book_copy_id CHAR(36)     NULL,
    CONSTRAINT pk_reservation PRIMARY KEY (id)
);

CREATE TABLE return_transaction
(
    id             CHAR(36)     NOT NULL,
    created_at     datetime     NULL,
    updated_at     datetime     NULL,
    created_by     VARCHAR(36)  NULL,
    updated_by     VARCHAR(36)  NULL,
    is_deleted     BIT(1)       NULL,
    return_at      datetime     NULL,
    book_condition VARCHAR(255) NULL,
    note           VARCHAR(255) NULL,
    loan_id        CHAR(36)     NULL,
    staff_id       CHAR(36)     NULL,
    CONSTRAINT pk_return_transaction PRIMARY KEY (id)
);

CREATE TABLE review
(
    id         CHAR(36)     NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    created_by VARCHAR(36)  NULL,
    updated_by VARCHAR(36)  NULL,
    is_deleted BIT(1)       NULL,
    rating     INT          NULL,
    comment    VARCHAR(255) NULL,
    student_id CHAR(36)     NULL,
    book_id    CHAR(36)     NULL,
    CONSTRAINT pk_review PRIMARY KEY (id)
);

CREATE TABLE `role`
(
    id            CHAR(36)     NOT NULL,
    created_at    datetime     NULL,
    updated_at    datetime     NULL,
    created_by    VARCHAR(36)  NULL,
    updated_by    VARCHAR(36)  NULL,
    is_deleted    BIT(1)       NULL,
    name          VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE staff
(
    id         CHAR(36)     NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    created_by VARCHAR(36)  NULL,
    updated_by VARCHAR(36)  NULL,
    is_deleted BIT(1)       NULL,
    staff_code VARCHAR(255) NOT NULL,
    status     VARCHAR(255) NULL,
    user_id    CHAR(36)     NULL,
    CONSTRAINT pk_staff PRIMARY KEY (id)
);

CREATE TABLE student
(
    id           CHAR(36)     NOT NULL,
    created_at   datetime     NULL,
    updated_at   datetime     NULL,
    created_by   VARCHAR(36)  NULL,
    updated_by   VARCHAR(36)  NULL,
    is_deleted   BIT(1)       NULL,
    student_code VARCHAR(255) NOT NULL,
    faculty      VARCHAR(255) NULL,
    clazz        VARCHAR(255) NULL,
    status       VARCHAR(255) NULL,
    fine_balance DOUBLE       NULL,
    user_id      CHAR(36)     NULL,
    CONSTRAINT pk_student PRIMARY KEY (id)
);

CREATE TABLE user
(
    id         CHAR(36)     NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    created_by VARCHAR(36)  NULL,
    updated_by VARCHAR(36)  NULL,
    is_deleted BIT(1)       NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NULL,
    full_name  VARCHAR(255) NULL,
    phone      VARCHAR(255) NULL,
    status     VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id Char(36) NOT NULL,
    role_id Char(36) NOT NULL,
    CONSTRAINT pk_userrole PRIMARY KEY (user_id, role_id)
);

ALTER TABLE book_copy
    ADD CONSTRAINT uc_book_copy_barcode UNIQUE (barcode);

ALTER TABLE book
    ADD CONSTRAINT uc_book_isbn UNIQUE (isbn);

ALTER TABLE return_transaction
    ADD CONSTRAINT uc_return_transaction_loan UNIQUE (loan_id);

ALTER TABLE `role`
    ADD CONSTRAINT uc_role_name UNIQUE (name);

ALTER TABLE staff
    ADD CONSTRAINT uc_staff_staffcode UNIQUE (staff_code);

ALTER TABLE staff
    ADD CONSTRAINT uc_staff_user UNIQUE (user_id);

ALTER TABLE student
    ADD CONSTRAINT uc_student_studentcode UNIQUE (student_code);

ALTER TABLE student
    ADD CONSTRAINT uc_student_user UNIQUE (user_id);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE book_copy
    ADD CONSTRAINT FK_BOOK_COPY_ON_BOOK FOREIGN KEY (book_id) REFERENCES book (id);

ALTER TABLE fine_payment
    ADD CONSTRAINT FK_FINEPAYMENT_ON_FINE FOREIGN KEY (fine_id) REFERENCES fine (id);

ALTER TABLE fine
    ADD CONSTRAINT FK_FINE_ON_LOAN FOREIGN KEY (loan_id) REFERENCES loan (id);

ALTER TABLE fine
    ADD CONSTRAINT FK_FINE_ON_STUDENT FOREIGN KEY (student_id) REFERENCES student (id);

ALTER TABLE loan
    ADD CONSTRAINT FK_LOAN_ON_BOOK_COPY FOREIGN KEY (book_copy_id) REFERENCES book_copy (id);

ALTER TABLE loan
    ADD CONSTRAINT FK_LOAN_ON_STUDENT FOREIGN KEY (student_id) REFERENCES student (id);

ALTER TABLE notification
    ADD CONSTRAINT FK_NOTIFICATION_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE reservation
    ADD CONSTRAINT FK_RESERVATION_ON_BOOK_COPY FOREIGN KEY (book_copy_id) REFERENCES book_copy (id);

ALTER TABLE reservation
    ADD CONSTRAINT FK_RESERVATION_ON_STUDENT FOREIGN KEY (student_id) REFERENCES student (id);

ALTER TABLE return_transaction
    ADD CONSTRAINT FK_RETURN_TRANSACTION_ON_LOAN FOREIGN KEY (loan_id) REFERENCES loan (id);

ALTER TABLE return_transaction
    ADD CONSTRAINT FK_RETURN_TRANSACTION_ON_STAFF FOREIGN KEY (staff_id) REFERENCES staff (id);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_BOOK FOREIGN KEY (book_id) REFERENCES book (id);

ALTER TABLE review
    ADD CONSTRAINT FK_REVIEW_ON_STUDENT FOREIGN KEY (student_id) REFERENCES student (id);

ALTER TABLE staff
    ADD CONSTRAINT FK_STAFF_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE student
    ADD CONSTRAINT FK_STUDENT_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE user_role
    ADD CONSTRAINT FK_USERROLE_ON_ROLE FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE user_role
    ADD CONSTRAINT FK_USERROLE_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);