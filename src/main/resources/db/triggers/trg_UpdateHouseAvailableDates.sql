CREATE TRIGGER trg_Payments_AfterInsert
    ON payments
    AFTER INSERT
AS
BEGIN
  SET NOCOUNT ON;
UPDATE reservations
SET status = 'APPROVED'
    FROM reservations r
  JOIN inserted p ON r.id = p.reservation_id;
END;
GO
