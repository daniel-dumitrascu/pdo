---------------------------------------------------------------------------
-- Function used to insert a basic people entity into the PEOPLE table
-- We should not call this directly but the derived functions from this one
---------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.AddPeople(inName TEXT, inNationality TEXT, inRole INT)
RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    DECLARE
        vPeople_Key INTEGER;
    BEGIN
        SELECT nextval('mspublic.people_people_key_seq'::regclass) INTO vPeople_Key;
        INSERT INTO PEOPLE(People_Key, NAME, Nationality, ROLE)
        VALUES
        (
            vPeople_Key,
            inName,
            inNationality,
            inRole
        );
        return vPeople_Key;
    END;
$function$;

-----------------------------------------------
-- Function used to return a people key by name
-----------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.GetPeopleKeyByName(inActorName TEXT)
RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    BEGIN
        return (SELECT (People_key) FROM People WHERE NAME = inActorName);
    END;
$function$;