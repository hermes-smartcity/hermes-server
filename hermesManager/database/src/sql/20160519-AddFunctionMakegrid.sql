CREATE OR REPLACE FUNCTION public.makegrid_2d(bound_polygon public.geometry, number_of_cells double precision)
RETURNS public.geometry AS
$body$
DECLARE
  BoundM public.geometry; --Bound polygon transformed to the metric projection (with metric_srid SRID)
  Xmin DOUBLE PRECISION;
  Xmax DOUBLE PRECISION;
  Ymin DOUBLE PRECISION;
  Ymax DOUBLE PRECISION;
  X DOUBLE PRECISION;
  Y DOUBLE PRECISION;
  sectors public.geometry[];
  i INTEGER;
  srid INTEGER;
  size DOUBLE PRECISION;
BEGIN
  Xmin := ST_XMin($1);
  Xmax := ST_XMax($1);
  Ymin := ST_YMin($1);
  Ymax := ST_YMax($1);
  srid := ST_srid($1);
  Y := Ymin; --current sector's corner coordinate
  i := -1;
  size := (Ymax - Ymin) / $2;
  <<yloop>>
  LOOP
    IF (Y > Ymax) THEN  --Better if generating polygons exceeds the bound for one step. You always can crop the result. But if not you may get not quite correct data for outbound polygons (e.g. if you calculate frequency per sector)
        EXIT;
    END IF;
    X := Xmin;
    <<xloop>>
    LOOP
      IF (X > Xmax) THEN
          EXIT;
      END IF;
      i := i + 1;
      sectors[i] := ST_GeomFromText('POLYGON(('||X||' '||Y||', '||(X+size)||' '||Y||', '||(X+size)||' '||(Y+size)||', '||X||' '||(Y+size)||', '||X||' '||Y||'))', srid);
      X := X + size;
    END LOOP xloop;
    Y := Y + size;
  END LOOP yloop;
  RETURN ST_Transform(ST_Collect(sectors), ST_SRID($1));
END;
$body$
LANGUAGE 'plpgsql';