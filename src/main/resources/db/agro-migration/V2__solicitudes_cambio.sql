alter table ag_labores add cantidad_jornales number(6,0) default 1 not null;

alter table ag_labores add constraint ck_ag_labor_jornales check (cantidad_jornales > 0);
