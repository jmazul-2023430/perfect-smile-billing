use perfect_smiles_billing_IN4AM;

call sp_create_role('owner', 'clinic owner');
call sp_create_role('administrator', 'system administrator');
call sp_create_role('dentist', 'dentist');

call sp_read_role();

call sp_edit_role('owner', 'main clinic owner', true, 1);
call sp_edit_role('administrator', 'system administrator account', true, 2);
call sp_edit_role('dentist', 'general dentist', true, 3);

call sp_create_permission('create_user', 'create users', 'users', true);
call sp_create_permission('edit_user', 'edit users', 'users', true);
call sp_create_permission('delete_user', 'delete users', 'users', true);
call sp_create_permission('view_user', 'view users', 'users', true);

call sp_read_permission();

call sp_create_role_permission(1, 1, true);
call sp_create_role_permission(1, 2, true);
call sp_create_role_permission(1, 3, true);
call sp_create_role_permission(1, 4, true);

call sp_read_role_permission();

call sp_create_user(1, 'owner', 'ZxQZzmxc+XCKhSNpwMxKj4scgA/tz6xt+bxgq1sSUlw=', 'Propietario del Sistema', 'owner@sonrisaperfecta.com', '50211111111', '2026-09-09 07:00:00', true);
call sp_create_user(2, 'admin', '+CjGjCZO76TOc41xZuRAvfrotb4qgoC3SBC6TimLtXM=', 'admin principal', 'admin@sonrisaperfecta.com', '50212345678', '2026-09-09 08:00:00', true);
call sp_create_user(3, 'dr.jose', 'dmc0l0CpQ6IliowIwWxNilVyZ/FbVU0lqUrWbNpByzI=', 'Dr. Jose Rosales', 'dr.jose@sonrisaperfecta.com', '50287654321', '2026-09-09 09:00:00', true);

call sp_read_user();

call sp_create_patient('juan', 'perez', '1234567890123', '50299887766', 'juan.perez@email.com', 'zona 1, ciudad de guatemala', true);
call sp_create_patient('maria', 'gonzalez', '9876543210987', '50288776655', 'maria.gonzalez@email.com', 'zona 10, ciudad de guatemala', true);
call sp_create_patient('pedro', 'ramirez', '4567891230456', '50277665544', 'pedro.ramirez@email.com', 'zona 4, mixco', true);

call sp_read_patient();

call sp_edit_patient('juan', 'perez actualizado', '1234567890123', '50299887766', 'juan.perez@email.com', 'zona 1, ciudad de guatemala', true, 1);
call sp_edit_patient('maria', 'gonzalez actualizada', '9876543210987', '50288776655', 'maria.gonzalez@email.com', 'zona 10, ciudad de guatemala', true, 2);
call sp_edit_patient('pedro', 'ramirez actualizado', '4567891230456', '50277665544', 'pedro.ramirez@email.com', 'zona 4, mixco', true, 3);

call sp_create_treatment(2, 'TRT-001', 'limpieza dental', 250.00, true, 'limpieza profesional de dientes y encias');
call sp_create_treatment(2, 'TRT-002', 'extraccion simple', 500.00, true, 'extraccion de pieza dental sin complicaciones');
call sp_create_treatment(3, 'TRT-003', 'obturacion composite', 350.00, true, 'restauracion dental con resina composite');

call sp_read_treatment();

call sp_edit_treatment(2, 'TRT-001', 'limpieza dental profunda', 300.00, true, 'limpieza con ultrasonido', 1);
call sp_edit_treatment(2, 'TRT-002', 'extraccion simple actualizada', 550.00, true, 'extraccion sin complicaciones', 2);
call sp_edit_treatment(3, 'TRT-003', 'obturacion composite avanzada', 400.00, true, 'restauracion con resina de alta calidad', 3);

call sp_create_budget(1, 2, 'Limpieza dental de rutina', '2026-09-01', 250.00, 30.00, 280.00, true);
call sp_create_budget(2, 3, 'Obturacion con resina', '2026-09-03', 350.00, 42.00, 392.00, true);
call sp_create_budget(3, 2, 'Extraccion simple de molar', '2026-09-05', 500.00, 60.00, 560.00, true);

call sp_read_budget();

call sp_edit_budget(1, 2, 'Limpieza dental profunda con ultrasonido', '2026-09-01', 300.00, 36.00, 336.00, true, 1);
call sp_edit_budget(2, 3, 'Obturacion avanzada de composite', '2026-09-03', 400.00, 48.00, 448.00, true, 2);
call sp_edit_budget(3, 2, 'Extraccion compleja de molar', '2026-09-05', 550.00, 66.00, 616.00, true, 3);

call sp_create_budget_detail(1, 1, 300.00, 1, 300.00);
call sp_create_budget_detail(2, 3, 400.00, 1, 400.00);
call sp_create_budget_detail(3, 2, 550.00, 1, 550.00);

call sp_read_budget_detail();