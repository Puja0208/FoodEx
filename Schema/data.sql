delete from student;
delete from "order";
delete from account;
delete from bills;
delete from dish;
delete from pending_dish_list;
delete from canteen;
delete from menu;
delete from reviews;
delete from stud_password;
delete from canteen_password;

insert into canteen values ('01', 'Queen of the campus', 'Hostel 1', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558575', 'true');
insert into canteen values ('02', 'The Wild Ones', 'Hostel 2', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558576', 'true');
insert into canteen values ('03', 'Vitruvians', 'Hostel 3', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558577', 'true');
insert into canteen values ('04', 'Madhouse', 'Hostel 4', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558578', 'true');
insert into canteen values ('05', 'Penthouse', 'Hostel 5', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558579', 'true');
insert into canteen values ('06', 'Vikings', 'Hostel 6', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558580', 'true');
insert into canteen values ('07', 'The Lady of the Lake ', 'Hostel 7', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558581', 'true');
insert into canteen values ('08', 'Woodlands', 'Hostel 8', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558582', 'true');
insert into canteen values ('09', 'Nawabo ki Basti', 'Hostel 9', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558583', 'true');
insert into canteen values ('10', 'Phoenix', 'Hostel 10', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558584', 'true');
insert into canteen values ('11', 'Athena', 'Hostel 11', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558585', 'true');
insert into canteen values ('12', 'Crown of the Campus', 'Hostel 12', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558586', 'true');
insert into canteen values ('13', 'House of Titans', 'Hostel 13', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558587', 'true');
insert into canteen values ('14', 'Silicon Ship', 'Hostel 14', 'MON-SUN, 11:00 to 15:00 ,18:00 to 3:00', '9458558588', 'true');
insert into canteen values ('15', 'Trident', 'Hostel 15', 'MON-SUN, ,22:00 to 3:00', '9458558589', 'true');
insert into canteen values ('16', 'Olympus', 'Hostel 16', 'MON-SUN, ,22:00 to 3:00', '9458558590', 'true');
insert into canteen values ('17', 'Amul Parlour', 'Hostel 12', 'MON-SUN, 08:00 to 23:00', '9458558500', 'true');
insert into canteen values ('18', 'Campus Hub', 'Hostel 5', 'MON-SUN, 09:00 to 04:00', '9458558444', 'true');

insert into canteen_account values ('0101', '0', '0', '01');
insert into canteen_account values ('0202', '0', '0', '02');
insert into canteen_account values ('0303', '0', '0', '03');
insert into canteen_account values ('0404', '0', '0', '04');
insert into canteen_account values ('0505', '0', '0', '05');
insert into canteen_account values ('0606', '0', '0', '06');
insert into canteen_account values ('0707', '0', '0', '07');
insert into canteen_account values ('0808', '0', '0', '08');
insert into canteen_account values ('0909', '0', '0', '09');
insert into canteen_account values ('1010', '0', '0', '10');
insert into canteen_account values ('1111', '0', '0', '11');
insert into canteen_account values ('1212', '0', '0', '12');
insert into canteen_account values ('1313', '0', '0', '13');
insert into canteen_account values ('1414', '0', '0', '14');
insert into canteen_account values ('1515', '0', '0', '15');
insert into canteen_account values ('1616', '0', '0', '16');
insert into canteen_account values ('1717', '0', '0', '17');
insert into canteen_account values ('1818', '0', '0', '18');

insert into canteen_password values ('01', '1234');
insert into canteen_password values ('02', '1234');
insert into canteen_password values ('03', '1234');
insert into canteen_password values ('04', '1234');
insert into canteen_password values ('05', '1234');
insert into canteen_password values ('06', '1234');
insert into canteen_password values ('07', '1234');
insert into canteen_password values ('08', '1234');
insert into canteen_password values ('09', '1234');
insert into canteen_password values ('10', '1234');
insert into canteen_password values ('11', '1234');
insert into canteen_password values ('12', '1234');
insert into canteen_password values ('13', '1234');
insert into canteen_password values ('14', '1234');
insert into canteen_password values ('15', '1234');
insert into canteen_password values ('16', '1234');
insert into canteen_password values ('17', '1234');
insert into canteen_password values ('18', '1234');

insert into dish values (nextval('dishid'), 'Masala Dosa', 'Veg', '15 min');
insert into dish values (nextval('dishid'), 'Frankie', 'Veg', '20 min');
insert into dish values (nextval('dishid'), 'Egg Bhurji', 'Non Veg', '15 min');
insert into dish values (nextval('dishid'), 'Maggi', 'Veg', '10 min');