-- UTILITY FUNCTIONS:
-- The ; that ends each sentence must be alone in a line so that the maven SQL plugin does not get confused

-- Create a function that always returns the first non-NULL item
CREATE OR REPLACE FUNCTION public.first_agg ( anyelement, anyelement )
RETURNS anyelement LANGUAGE SQL IMMUTABLE STRICT AS $$
        SELECT $1;
$$
;
 
-- And then wrap an aggregate around it
DROP AGGREGATE IF EXISTS public.FIRST(anyelement)
;
CREATE AGGREGATE public.FIRST (
        sfunc    = public.first_agg,
        basetype = anyelement,
        stype    = anyelement
)
;
 
-- Create a function that always returns the last non-NULL item
CREATE OR REPLACE FUNCTION public.last_agg ( anyelement, anyelement )
RETURNS anyelement LANGUAGE SQL IMMUTABLE STRICT AS $$
        SELECT $2;
$$
;
 
-- And then wrap an aggregate around it
DROP AGGREGATE IF EXISTS public.LAST(anyelement)
;
CREATE AGGREGATE public.LAST (
        sfunc    = public.last_agg,
        basetype = anyelement,
        stype    = anyelement
)
;

CREATE OR REPLACE FUNCTION castToInt(text) RETURNS integer AS $$
BEGIN
    -- Note the double casting to avoid infinite recursion.
    RETURN cast($1::varchar AS integer);
exception
    WHEN invalid_text_representation THEN
        RETURN 0;
END;
$$ LANGUAGE plpgsql immutable
;

DROP CAST IF EXISTS (text AS integer)
;
CREATE cast (text AS integer) WITH FUNCTION castToInt(text)
;

CREATE OR REPLACE FUNCTION nearestNode(table_name text, x_long double precision, y_lat double precision, OUT nodo bigint) AS $$
BEGIN
    EXECUTE format('SELECT id FROM %I ORDER BY geometry <-> ''POINT(%s %s)''::geometry(Point) LIMIT 1', table_name, x_long, y_lat) INTO nodo;
END;
$$ LANGUAGE plpgsql
;

CREATE OR REPLACE FUNCTION normalizeAngle(a double precision) RETURNS double precision AS $$
DECLARE
	turns int := sign(a)*(abs(trunc(a/360)) + (sign(a) = -1)::int);
BEGIN
	RETURN a - turns*360;
END;
$$ LANGUAGE plpgsql
;

CREATE OR REPLACE FUNCTION compareSlope(a double precision, b double precision) RETURNS double precision AS $$
BEGIN
	RETURN CASE
	WHEN a - b > 270 THEN a - b - 360
	WHEN a - b > 90 THEN a - b - 180
	WHEN a - b < -270 THEN a - b + 360
	WHEN a - b < -90 THEN a - b + 180
	ELSE a - b END;
END;
$$ LANGUAGE plpgsql
;