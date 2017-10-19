CREATE OR REPLACE TRIGGER tri_ajout
AFTER INSERT ON LesMaladies
FOR EACH ROW
DECLARE
BEGIN
	UPDATE LesAnimaux SET nb_maladies = nb_maladies + 1 WHERE LesAnimaux.nomA = :NEW.nomA;
END;
/

CREATE OR REPLACE TRIGGER tri_suppr
AFTER DELETE ON LesMaladies
FOR EACH ROW
DECLARE
BEGIN
	UPDATE LesAnimaux SET nb_maladies = nb_maladies - 1 WHERE LesAnimaux.nomA = :OLD.nomA;
END;
/

CREATE OR REPLACE TRIGGER tri_upd
AFTER UPDATE ON LesMaladies
FOR EACH ROW
DECLARE
BEGIN
	UPDATE LesAnimaux SET nb_maladies = nb_maladies + 1 WHERE LesAnimaux.nomA = :NEW.nomA;
	UPDATE LesAnimaux SET nb_maladies = nb_maladies - 1 WHERE LesAnimaux.nomA = :OLD.nomA;
END;
/

CREATE OR REPLACE TRIGGER checkCage
BEFORE INSERT OR UPDATE ON LesAnimaux
FOR EACH ROW
DECLARE
	fonc_cage varchar2(20);
BEGIN

	SELECT fonction into fonc_cage FROM LesCages WHERE noCage = :NEW.noCage;
	if(fonc_cage != :NEW.fonction_cage) then
		RAISE_APPLICATION_ERROR(-99999, 'Mauvaise fonction de cage');
	end if;
	
END;
/



