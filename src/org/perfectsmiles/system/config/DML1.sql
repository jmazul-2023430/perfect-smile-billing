use perfect_smiles_billing_IN4AM;

-- NOTE: only 3 roles exist in this clinic: owner, administrator, dentist
-- (previously receptionist/assistant were included by mistake).

-- NOTE: this DML matches the CURRENT DDL (role_tb/permission_tb/... with
-- _tb suffix, explicit status columns, and sp_delete_* doing SOFT delete,
-- i.e. "UPDATE ... status = false" instead of removing the row -- except
-- sp_delete_budget_detail, which still does a real DELETE).
-- Two things had to change here versus the previous version:
--   1) sp_edit_role now takes 4 args (name, description, status, id),
--      not 3 -- it was missing the new p_status parameter.
--   2) role_permission_tb now has UNIQUE(id_role, id_permission), so the
--      throwaway demo row at the bottom can't reuse (1,4) (already used
--      by the real seed data) -- switched it to (2,1), which is free
--      because "administrator" has no permissions assigned yet.

-- NOTE: the original script deleted rows (roles, permissions, users) that
-- were later referenced as foreign keys by role_permission/treatment/budget,
-- which would fail with a FOREIGN KEY constraint error when run in order.
-- Restructured so all "keep" data is created first, and DELETE is only
-- demonstrated at the end on dedicated throwaway rows. With the DDL now
-- using soft-deletes for most tables this is less strictly necessary for
-- FK safety, but it's kept for consistency and because budget_detail's
-- delete is still a real DELETE.

-- ================================= role =================================
call sp_create_role('owner', 'clinic owner');
call sp_create_role('administrator', 'system administrator');
call sp_create_role('dentist', 'dentist');

call sp_read_role();

call sp_edit_role('owner', 'main clinic owner', true, 1);
call sp_edit_role('administrator', 'system administrator account', true, 2);
call sp_edit_role('dentist', 'general dentist', true, 3);

-- ================================= permission =================================
call sp_create_permission('create_user', 'create users', 'users', true);
call sp_create_permission('edit_user', 'edit users', 'users', true);
call sp_create_permission('delete_user', 'delete users', 'users', true);
call sp_create_permission('view_user', 'view users', 'users', true);

call sp_read_permission();

call sp_edit_permission('create user', 'permission to create users', 'users', true, 1);
call sp_edit_permission('edit user', 'permission to edit users', 'users', true, 2);
call sp_edit_permission('delete user', 'permission to delete users', 'users', true, 3);

-- ================================= role_permission =================================
-- owner (id_role = 1) is the ONLY one who manages users (create/edit/delete/view)
call sp_create_role_permission(1, 1, true);
call sp_create_role_permission(1, 2, true);
call sp_create_role_permission(1, 3, true);
call sp_create_role_permission(1, 4, true);

call sp_read_role_permission();

call sp_edit_role_permission(1, 1, false, 1);
call sp_edit_role_permission(1, 2, false, 2);
call sp_edit_role_permission(1, 3, false, 3);

-- ================================= user =================================
-- admin -> administrator role, dentists -> dentist role
call sp_create_user(2, 'admin', 'hash123', 'admin sistema', 'admin@sonrisaperfecta.com', '50212345678', '2026-09-09 08:00:00', true);
call sp_create_user(3, 'dr.garcia', 'hash456', 'dr. carlos garcia', 'garcia@sonrisaperfecta.com', '50287654321', '2026-09-09 09:00:00', true);
call sp_create_user(3, 'dr.lopez', 'hash789', 'dra. maria lopez', 'lopez@sonrisaperfecta.com', '50211223344', '2026-09-09 09:30:00', true);

call sp_read_user();

call sp_edit_user(2, 'admin', 'hash123', 'admin principal', 'admin@sonrisaperfecta.com', '50212345678', '2026-09-09 08:00:00', true, 1);
call sp_edit_user(3, 'dr.garcia', 'hash456', 'dr. carlos garcia actualizado', 'garcia@sonrisaperfecta.com', '50287654321', '2026-09-09 09:00:00', true, 2);
call sp_edit_user(3, 'dr.lopez', 'hash789', 'dra. maria lopez actualizada', 'lopez@sonrisaperfecta.com', '50211223344', '2026-09-09 09:30:00', true, 3);

-- ================================= patient =================================
call sp_create_patient('juan', 'perez', '1234567890123', '50299887766', 'juan.perez@email.com', 'zona 1, ciudad de guatemala', true);
call sp_create_patient('maria', 'gonzalez', '9876543210987', '50288776655', 'maria.gonzalez@email.com', 'zona 10, ciudad de guatemala', true);
call sp_create_patient('pedro', 'ramirez', '4567891230456', '50277665544', 'pedro.ramirez@email.com', 'zona 4, mixco', true);

call sp_read_patient();

call sp_edit_patient('juan', 'perez actualizado', '1234567890123', '50299887766', 'juan.perez@email.com', 'zona 1, ciudad de guatemala', true, 1);
call sp_edit_patient('maria', 'gonzalez actualizada', '9876543210987', '50288776655', 'maria.gonzalez@email.com', 'zona 10, ciudad de guatemala', true, 2);
call sp_edit_patient('pedro', 'ramirez actualizado', '4567891230456', '50277665544', 'pedro.ramirez@email.com', 'zona 4, mixco', true, 3);

-- ================================= treatment =================================
-- created by the dentists (id_user 2 and 3), not the admin
call sp_create_treatment(2, 'TRT-001', 'limpieza dental', 250.00, true, 'limpieza profesional de dientes y encias');
call sp_create_treatment(2, 'TRT-002', 'extraccion simple', 500.00, true, 'extraccion de pieza dental sin complicaciones');
call sp_create_treatment(3, 'TRT-004', 'obturacion composite', 350.00, true, 'restauracion dental con resina composite');

call sp_read_treatment();

call sp_edit_treatment(2, 'TRT-001', 'limpieza dental profunda', 300.00, true, 'limpieza con ultrasonido', 1);
call sp_edit_treatment(2, 'TRT-002', 'extraccion simple actualizada', 550.00, true, 'extraccion sin complicaciones', 2);
call sp_edit_treatment(3, 'TRT-004', 'obturacion composite avanzada', 400.00, true, 'restauracion con resina de alta calidad', 3);

-- ================================= budget =================================
-- one budget per patient, issued by the treating dentist
call sp_create_budget(1, 2, '2026-09-01', 250.00, 30.00, 280.00, true);
call sp_create_budget(2, 3, '2026-09-03', 350.00, 42.00, 392.00, true);
call sp_create_budget(3, 2, '2026-09-05', 500.00, 60.00, 560.00, true);

call sp_read_budget();

call sp_edit_budget(1, 2, '2026-09-01', 300.00, 36.00, 336.00, true, 1);
call sp_edit_budget(2, 3, '2026-09-03', 400.00, 48.00, 448.00, true, 2);
call sp_edit_budget(3, 2, '2026-09-05', 550.00, 66.00, 616.00, true, 3);

-- ================================= budget_detail =================================
call sp_create_budget_detail(1, 1, 250.00, 1, 250.00);
call sp_create_budget_detail(2, 3, 350.00, 1, 350.00);
call sp_create_budget_detail(3, 2, 500.00, 1, 500.00);

call sp_read_budget_detail();

call sp_edit_budget_detail(1, 1, 300.00, 1, 300.00, 1);
call sp_edit_budget_detail(2, 3, 400.00, 1, 400.00, 2);
call sp_edit_budget_detail(3, 2, 550.00, 1, 550.00, 3);

-- ================================= DELETE / DEACTIVATE DEMONSTRATION =================================
-- Dedicated throwaway rows only, removed/deactivated child -> parent, so
-- none of the "real" seed data above is ever touched. Most sp_delete_*
-- calls below are now SOFT deletes (they just flip *_status to false);
-- sp_delete_budget_detail is the one exception that truly removes the row.

call sp_create_treatment(2, 'TRT-999', 'temporary treatment', 100.00, true, 'test row to delete');
call sp_create_budget(1, 2, '2026-09-10', 100.00, 12.00, 112.00, true);
call sp_create_budget_detail(4, 4, 100.00, 1, 100.00);

call sp_delete_budget_detail(4);
call sp_delete_budget(4);
call sp_delete_treatment(4);

call sp_create_patient('demo', 'delete', '0000000000000', '50200000000', 'demo@delete.com', 'zone 0', true);
call sp_delete_patient(4);

call sp_create_user(2, 'demo.delete', 'hashdemo', 'temporary user', 'demo.delete@sonrisaperfecta.com', '50200000001', '2026-09-10 08:00:00', true);
call sp_delete_user(4);

call sp_create_role_permission(2, 1, true);
call sp_delete_role_permission(5);

call sp_create_permission('demo_permission', 'temporary permission', 'demo', true);
call sp_delete_permission(5);

call sp_create_role('demo_role', 'temporary role for testing');
call sp_delete_role(4);