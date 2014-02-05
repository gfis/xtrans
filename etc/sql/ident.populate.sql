-- Generate table rows for the "ident" grammar
-- @(#) $Id: parse_states.populate.sql 458 2010-06-01 21:38:42Z gfis $
-- 2010-06-08, Georg Fischer
-- State 1: initial
insert into items values(1, 'se', 'program', 'i', 2);
insert into items
	select distinct 2
	, elemtype
	, case when elemtype in ('id', 'str', 'num', 'chr') then '*' else elemval end
	, 'i'
	, 2
	from progelem 
	where elemtype in ('op', 'kw')
	;
insert into items values(2, 'ee', 'program', 'a', 3);
commit;
