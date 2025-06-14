CREATE TRIGGER trg_Reservations_UpdateTimestamp
    ON reservations
    AFTER UPDATE
              AS
BEGIN
  SET NOCOUNT ON;
UPDATE reservations
SET updated_at = GETDATE()
    FROM reservations r
  JOIN inserted i ON r.id = i.id;
END;
GO
