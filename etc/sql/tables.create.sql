DROP   TABLE items;
CREATE TABLE items
		( grammar	VARCHAR(16) NOT NULL
		, state	INT NOT NULL
		, elemtype	VARCHAR( 8) NOT NULL
		, elemval	VARCHAR(64) NOT NULL
		, action	CHAR(1) NOT NULL
		, next_info	INT
		, PRIMARY KEY(grammar, state, elemtype, elemval)
		);
COMMIT;
DROP   TABLE productions;
CREATE TABLE productions
		( grammar	VARCHAR(16) NOT NULL
		, prod_no	INT NOT NULL
		, left_side	VARCHAR(64) NOT NULL
		, action	CHAR(1)
		, right_length	INT
		, PRIMARY KEY(grammar, prod_no)
		);
COMMIT;
