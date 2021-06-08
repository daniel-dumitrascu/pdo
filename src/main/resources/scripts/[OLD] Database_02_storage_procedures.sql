-- Functions present in this file:

-- AddNewStorage
-- CreateBackupLink
-- GetKeyByStorageName

----------------------------------------------------------------
-- Function used to insert a new hardware into the STORAGE table
----------------------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.AddNewStorage(inStorageName TEXT, inTypeOfStorage INT,  inStorageInUse BOOLEAN, inInUseStarting TEXT, inHasErrors BOOLEAN, inObservations TEXT)
RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    DECLARE
        vStorageKey INTEGER;
    BEGIN
        -- insert the new data only if we don't have that hardware in the database
        -- so, we need to check to see of we have inStorageName in STORAGE table
        IF EXISTS (SELECT 1 FROM mspublic.STORAGE WHERE Storage_Name = inStorageName) THEN
           return NULL;
        ELSE 
           SELECT nextval('mspublic.storage_storage_key_seq'::regclass) INTO vStorageKey;
           INSERT INTO mspublic.STORAGE(Storage_Key,
                                        Storage_Name, 
                                        Type_Of_Storage, 
                                        Storage_In_Use, 
                                        In_Use_Starting, 
                                        Has_Errors, 
                                        Observations) 
           VALUES 
           (
                vStorageKey,
                inStorageName,
                inTypeOfStorage,
                inStorageInUse,
                to_date(inInUseStarting, 'DD Mon YYYY'),
                inHasErrors,
                inObservations
           );
           return vStorageKey;
        END IF;
    END;
$function$;



----------------------------------------------------------------------
-- Function used to insert a new backup hardware into the BACKUP table
----------------------------------------------------------------------

-- ca si param functia va lua numele unui hardware A (main Storage), si B (backup_storage)
-- 1. vom cauta numele hardware-ului A in tabela STORAGE
-- 1.1 daca il gasim mergem la punctul 2.
-- 1.2 daca nu il gasim iesim din functie intorcand -1
-- 2. vom cauta numele hardware-ului B in tabela STORAGE
-- 2.1 daca il gasim mergem la punctul 3.
-- 2.2 daca nu il gasim iesim din functie intorcand -1
-- 3. generam un noua cheie pt tabela BACKUP 
-- 3. inseram cele 3 chei intr-o noua intrare in tabela BACKUP

CREATE OR REPLACE FUNCTION mspublic.CreateBackupLink(inMainStorageName TEXT, inBackupStorageName TEXT)
RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    DECLARE
        vBackupKey INTEGER;
        vMainStorageKey INTEGER;
        vBackupStorageKey INTEGER;
    BEGIN
        SELECT (Storage_Key) INTO vMainStorageKey FROM STORAGE WHERE Storage_Name = inMainStorageName;
        IF vMainStorageKey IS NOT NULL THEN
            SELECT (Storage_Key) INTO vBackupStorageKey FROM STORAGE WHERE Storage_Name = inBackupStorageName;
            IF vBackupStorageKey IS NOT NULL AND (vMainStorageKey <> vBackupStorageKey) THEN
                SELECT nextval('mspublic.backup_backup_key_seq'::regclass) INTO vBackupKey;
                INSERT INTO BACKUP(Backup_Key,
                                    Main_Storage, 
                                    Backup_Storage) 
                VALUES 
                (
                    vBackupKey,
                    vMainStorageKey,
                    vBackupStorageKey
                );
                return vBackupKey;
            ELSE
                return NULL;
            END IF;
        ELSE 
           return NULL;
        END IF;
    END;
$function$;

------------------------------------------------------------
-- Get the key of a storage using the name of it.
-- If the name is not found in the table then we return NULL
------------------------------------------------------------
CREATE OR REPLACE FUNCTION mspublic.GetKeyByStorageName(inStorageName TEXT)
RETURNS INTEGER LANGUAGE plpgsql
AS $function$
    DECLARE
        vStorageKey INTEGER;
    BEGIN
        SELECT (Storage_Key) FROM STORAGE WHERE Storage_Name = inStorageName INTO vStorageKey;
        return vStorageKey;
    END;
$function$;