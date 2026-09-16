drop database if exists perfect_smiles_billing_IN4AM;
create database perfect_smiles_billing_IN4AM;
use perfect_smiles_billing_IN4AM;

-- =================================================================
-- TABLES CREATION
-- =================================================================

create table role_tb (
    id_role int primary key auto_increment,
    role_name varchar(50) not null,
    role_description varchar(100),
    role_status boolean default true
);

create table permission_tb (
    id_permission int primary key auto_increment,
    permission_name varchar(50) not null,
    permission_description varchar(50),
    module_name varchar(50),
    permission_status boolean default true
);

create table role_permission_tb (
    id_role_permission int primary key auto_increment,
    id_role int not null,
    id_permission int not null,
    assignment_status boolean default true,
    foreign key (id_role) references role_tb(id_role) on delete cascade,
    foreign key (id_permission) references permission_tb(id_permission) on delete cascade,
    unique key uk_role_permission (id_role, id_permission)
);

create table user_tb (
    id_user int primary key auto_increment,
    id_role int not null,
    username varchar(50) not null unique,
    password_hash varchar(255) not null,
    full_name varchar(100) not null,
    email varchar(100) unique,
    phone varchar(15),
    last_access datetime,
    user_status boolean default true,
    foreign key (id_role) references role_tb(id_role)
);

create table patient_tb (
    id_patient int primary key auto_increment,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    dpi varchar(20) unique,
    phone varchar(15),
    email varchar(100),
    address varchar(100),
    patient_status boolean default true
);

create table treatment_tb (
    id_treatment int primary key auto_increment,
    id_user int not null,
    internal_code varchar(20),
    treatment_name varchar(120) not null,
    standard_cost decimal(10, 2),
    treatment_status boolean default true,
    treatment_description text,
    foreign key (id_user) references user_tb(id_user)
);

create table budget_tb (
    id_budget int primary key auto_increment,
    id_patient int not null,
    id_user int not null,
    issue_date date not null,
    subtotal decimal(10, 2),
    tax decimal(10, 2),
    total decimal(10, 2),
    budget_status boolean default true,
    foreign key (id_patient) references patient_tb(id_patient),
    foreign key (id_user) references user_tb(id_user)
);

create table budget_detail_tb (
    id_budget_detail int primary key auto_increment,
    id_budget int not null,
    id_treatment int not null,
    unit_price decimal(10, 2) not null,
    item_quantity int not null,
    subtotal decimal(10, 2),
    foreign key (id_budget) references budget_tb(id_budget) on delete cascade,
    foreign key (id_treatment) references treatment_tb(id_treatment)
);

-- =================================================================
-- STORED PROCEDURES
-- =================================================================

-- -----------------------------------------------------------------
-- Role Procedures
-- -----------------------------------------------------------------
delimiter $$
create procedure sp_create_role(in p_role_name varchar(50), in p_description varchar(100))
begin
    insert into role_tb(role_name, role_description)
        values(p_role_name, p_description);
end $$
delimiter ;

delimiter $$
create procedure sp_read_role()
begin
    select
        id_role as ID_ROLE,
        role_name as `Role Name`,
        role_description as Description,
        role_status as Status
        from role_tb;
end $$
delimiter ;

delimiter $$
create procedure sp_edit_role(in p_role_name varchar(50), in p_description varchar(100), in p_status boolean, in p_id_role int)
begin
    update role_tb set
        role_name = p_role_name,
        role_description = p_description,
        role_status = p_status
        where id_role = p_id_role;
end $$
delimiter ;

delimiter $$
create procedure sp_delete_role(in p_id_role int)
begin
    update role_tb set
        role_status = false
        where id_role = p_id_role;
end $$
delimiter ;

-- -----------------------------------------------------------------
-- Permission Procedures
-- -----------------------------------------------------------------
delimiter $$
create procedure sp_create_permission(in p_permission_name varchar(50), in p_description varchar(50), in p_module varchar(50), in p_status boolean)
begin
    insert into permission_tb(permission_name, permission_description, module_name, permission_status)
        values(p_permission_name, p_description, p_module, p_status);
end $$
delimiter ;

delimiter $$
create procedure sp_read_permission()
begin
    select
        id_permission as ID_PERMISSION,
        permission_name as `Permission Name`,
        permission_description as Description,
        module_name as Module,
        permission_status as Status
        from permission_tb;
end $$
delimiter ;

delimiter $$
create procedure sp_edit_permission(in p_permission_name varchar(50), in p_description varchar(50), in p_module varchar(50), in p_status boolean, in p_id_permission int)
begin
    update permission_tb set
        permission_name = p_permission_name,
        permission_description = p_description,
        module_name = p_module,
        permission_status = p_status
        where id_permission = p_id_permission;
end $$
delimiter ;

delimiter $$
create procedure sp_delete_permission(in p_id_permission int)
begin
    update permission_tb set
        permission_status = false
        where id_permission = p_id_permission;
end $$
delimiter ;

-- -----------------------------------------------------------------
-- Role_Permission Procedures
-- -----------------------------------------------------------------
delimiter $$
create procedure sp_create_role_permission(in p_id_role int, in p_id_permission int, in p_status boolean)
begin
    insert into role_permission_tb(id_role, id_permission, assignment_status)
        values(p_id_role, p_id_permission, p_status);
end $$
delimiter ;

delimiter $$
create procedure sp_read_role_permission()
begin
    select
        id_role_permission as ID_ROLE_PERMISSION,
        id_role as ID_ROLE,
        id_permission as ID_PERMISSION,
        assignment_status as Status
        from role_permission_tb;
end $$
delimiter ;

delimiter $$
create procedure sp_edit_role_permission(in p_id_role int, in p_id_permission int, in p_status boolean, in p_id_role_permission int)
begin
    update role_permission_tb set
        id_role = p_id_role,
        id_permission = p_id_permission,
        assignment_status = p_status
        where id_role_permission = p_id_role_permission;
end $$
delimiter ;

delimiter $$
create procedure sp_delete_role_permission(in p_id_role_permission int)
begin
    update role_permission_tb set
        assignment_status = false
        where id_role_permission = p_id_role_permission;
end $$
delimiter ;

-- -----------------------------------------------------------------
-- User Procedures
-- -----------------------------------------------------------------
delimiter $$
create procedure sp_create_user(in p_id_role int, in p_username varchar(50), in p_password_hash varchar(255), in p_full_name varchar(100), in p_email varchar(100), in p_phone varchar(15), in p_last_access datetime, in p_status boolean)
begin
    insert into user_tb(id_role, username, password_hash, full_name, email, phone, last_access, user_status)
        values(p_id_role, p_username, p_password_hash, p_full_name, p_email, p_phone, p_last_access, p_status);
end $$
delimiter ;

delimiter $$
create procedure sp_read_user()
begin
    select
        id_user as ID_USER,
        id_role as ID_ROLE,
        username as Username,
        full_name as `Full Name`,
        email as Email,
        phone as Phone,
        last_access as `Last Access`,
        user_status as Status
        from user_tb;
end $$
delimiter ;

delimiter $$
create procedure sp_edit_user(in p_id_role int, in p_username varchar(50), in p_password_hash varchar(255), in p_full_name varchar(100), in p_email varchar(100), in p_phone varchar(15), in p_last_access datetime, in p_status boolean, in p_id_user int)
begin
    update user_tb set
        id_role = p_id_role,
        username = p_username,
        password_hash = p_password_hash,
        full_name = p_full_name,
        email = p_email,
        phone = p_phone,
        last_access = p_last_access,
        user_status = p_status
        where id_user = p_id_user;
end $$
delimiter ;

delimiter $$
create procedure sp_delete_user(in p_id_user int)
begin
    update user_tb set
        user_status = false
        where id_user = p_id_user;
end $$
delimiter ;

-- -----------------------------------------------------------------
-- Patient Procedures
-- -----------------------------------------------------------------
delimiter $$
create procedure sp_create_patient(in p_first_name varchar(100), in p_last_name varchar(100), in p_dpi varchar(20), in p_phone varchar(15), in p_email varchar(100), in p_address varchar(100), in p_status boolean)
begin
    insert into patient_tb(first_name, last_name, dpi, phone, email, address, patient_status)
        values(p_first_name, p_last_name, p_dpi, p_phone, p_email, p_address, p_status);
end $$
delimiter ;

delimiter $$
create procedure sp_read_patient()
begin
    select
        id_patient as ID_PATIENT,
        first_name as `First Name`,
        last_name as `Last Name`,
        dpi as DPI,
        phone as Phone,
        email as Email,
        address as Address,
        patient_status as Status
        from patient_tb;
end $$
delimiter ;

delimiter $$
create procedure sp_edit_patient(in p_first_name varchar(100), in p_last_name varchar(100), in p_dpi varchar(20), in p_phone varchar(15), in p_email varchar(100), in p_address varchar(100), in p_status boolean, in p_id_patient int)
begin
    update patient_tb set
        first_name = p_first_name,
        last_name = p_last_name,
        dpi = p_dpi,
        phone = p_phone,
        email = p_email,
        address = p_address,
        patient_status = p_status
        where id_patient = p_id_patient;
end $$
delimiter ;

delimiter $$
create procedure sp_delete_patient(in p_id_patient int)
begin
    update patient_tb set
        patient_status = false
        where id_patient = p_id_patient;
end $$
delimiter ;

-- -----------------------------------------------------------------
-- Treatment Procedures
-- -----------------------------------------------------------------
delimiter $$
create procedure sp_create_treatment(in p_id_user int, in p_internal_code varchar(20), in p_treatment_name varchar(120), in p_standard_cost decimal(10,2), in p_status boolean, in p_description text)
begin
    insert into treatment_tb(id_user, internal_code, treatment_name, standard_cost, treatment_status, treatment_description)
        values(p_id_user, p_internal_code, p_treatment_name, p_standard_cost, p_status, p_description);
end $$
delimiter ;

delimiter $$
create procedure sp_read_treatment()
begin
    select
        id_treatment as ID_TREATMENT,
        id_user as ID_USER,
        internal_code as `Internal Code`,
        treatment_name as `Treatment Name`,
        standard_cost as `Standard Cost`,
        treatment_status as Status,
        treatment_description as Description
        from treatment_tb;
end $$
delimiter ;

delimiter $$
create procedure sp_edit_treatment(in p_id_user int, in p_internal_code varchar(20), in p_treatment_name varchar(120), in p_standard_cost decimal(10,2), in p_status boolean, in p_description text, in p_id_treatment int)
begin
    update treatment_tb set
        id_user = p_id_user,
        internal_code = p_internal_code,
        treatment_name = p_treatment_name,
        standard_cost = p_standard_cost,
        treatment_status = p_status,
        treatment_description = p_description
        where id_treatment = p_id_treatment;
end $$
delimiter ;

delimiter $$
create procedure sp_delete_treatment(in p_id_treatment int)
begin
    update treatment_tb set
        treatment_status = false
        where id_treatment = p_id_treatment;
end $$
delimiter ;

-- -----------------------------------------------------------------
-- Budget Procedures
-- -----------------------------------------------------------------
delimiter $$
create procedure sp_create_budget(in p_id_patient int, in p_id_user int, in p_issue_date date, in p_subtotal decimal(10,2), in p_tax decimal(10,2), in p_total decimal(10,2), in p_status boolean)
begin
    insert into budget_tb(id_patient, id_user, issue_date, subtotal, tax, total, budget_status)
        values(p_id_patient, p_id_user, p_issue_date, p_subtotal, p_tax, p_total, p_status);
end $$
delimiter ;

delimiter $$
create procedure sp_read_budget()
begin
    select
        id_budget as ID_BUDGET,
        id_patient as ID_PATIENT,
        id_user as ID_USER,
        issue_date as `Issue Date`,
        subtotal as Subtotal,
        tax as Tax,
        total as Total,
        budget_status as Status
        from budget_tb;
end $$
delimiter ;

delimiter $$
create procedure sp_edit_budget(in p_id_patient int, in p_id_user int, in p_issue_date date, in p_subtotal decimal(10,2), in p_tax decimal(10,2), in p_total decimal(10,2), in p_status boolean, in p_id_budget int)
begin
    update budget_tb set
        id_patient = p_id_patient,
        id_user = p_id_user,
        issue_date = p_issue_date,
        subtotal = p_subtotal,
        tax = p_tax,
        total = p_total,
        budget_status = p_status
        where id_budget = p_id_budget;
end $$
delimiter ;

delimiter $$
create procedure sp_delete_budget(in p_id_budget int)
begin
    update budget_tb set
        budget_status = false
        where id_budget = p_id_budget;
end $$
delimiter ;

-- -----------------------------------------------------------------
-- Budget_Detail Procedures
-- -----------------------------------------------------------------
delimiter $$
create procedure sp_create_budget_detail(in p_id_budget int, in p_id_treatment int, in p_unit_price decimal(10,2), in p_quantity int, in p_subtotal decimal(10,2))
begin
    insert into budget_detail_tb(id_budget, id_treatment, unit_price, item_quantity, subtotal)
        values(p_id_budget, p_id_treatment, p_unit_price, p_quantity, p_subtotal);
end $$
delimiter ;

delimiter $$
create procedure sp_read_budget_detail()
begin
    select
        id_budget_detail as ID_BUDGET_DETAIL,
        id_budget as ID_BUDGET,
        id_treatment as ID_TREATMENT,
        unit_price as `Unit Price`,
        item_quantity as Quantity,
        subtotal as Subtotal
        from budget_detail_tb;
end $$
delimiter ;

delimiter $$
create procedure sp_edit_budget_detail(in p_id_budget int, in p_id_treatment int, in p_unit_price decimal(10,2), in p_quantity int, in p_subtotal decimal(10,2), in p_id_budget_detail int)
begin
    update budget_detail_tb set
        id_budget = p_id_budget,
        id_treatment = p_id_treatment,
        unit_price = p_unit_price,
        item_quantity = p_quantity,
        subtotal = p_subtotal
        where id_budget_detail = p_id_budget_detail;
end $$
delimiter ;

delimiter $$
create procedure sp_delete_budget_detail(in p_id_budget_detail int)
begin
    delete from budget_detail_tb
        where id_budget_detail = p_id_budget_detail;
end $$
delimiter ;