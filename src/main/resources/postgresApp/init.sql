DROP SCHEMA IF EXISTS public CASCADE ;
CREATE SCHEMA public;

CREATE TABLE public.cinemas (
  id          SERIAL      NOT NULL,
  cinema_name VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (cinema_name)
);
CREATE TABLE public.halls (
  id        SERIAL      NOT NULL,
  cinema_id INT         NOT NULL,
  hall_name VARCHAR(32) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (hall_name),
  FOREIGN KEY (cinema_id) REFERENCES public.cinemas (id)
);

CREATE INDEX cinemas_name
  ON public.cinemas
  USING hash (cinema_name);
CREATE INDEX halls_name
  ON public.halls
  USING hash (hall_name);

CREATE TABLE public.places (
  id       SERIAL,
  hall_id  INT     NOT NULL,
  order_id INT     NOT NULL,
  state    BOOLEAN NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (hall_id) REFERENCES public.halls (id)
);

INSERT INTO public.cinemas (cinema_name)
VALUES ('CINEMA');

INSERT INTO public.halls (cinema_id, hall_name)
VALUES (1, 'earth'),
       (1, 'sun');

INSERT INTO public.places (hall_id, order_id, state)
VALUES (1, 1, false),
       (1, 2, false),
       (1, 3, true),
       (1, 4, true),
       (2, 1, false),
       (2, 2, false),
       (2, 3, true),
       (2, 4, true);