SELECT seq, centerline_geometry FROM pgr_astar('SELECT link_id id, s_id::int source, t_id::int target, 
		(CASE WHEN link_direction = ''inOppositeDirection'' THEN 10000 ELSE ST_Length(link_geometry)::float END) AS cost, 
		ST_x(s_geometry) x1, ST_y(s_geometry) y1, ST_x(t_geometry) x2, ST_y(t_geometry) y2,  
		(CASE WHEN link_direction = ''inDirection'' THEN 10000 ELSE ST_Length(link_geometry)::float END) AS reverse_cost FROM h_link_seq'::text,
	(SELECT nearestNode('hermes_transport_node'::text, %x1%, %y1%))::int,
	(SELECT nearestNode('hermes_transport_node'::text, %x2%, %y2%))::int,
	true, true) r JOIN hermes_transport_link htl ON htl.id = r.id2 ORDER BY seq
