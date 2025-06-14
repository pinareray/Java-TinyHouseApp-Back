CREATE FUNCTION dbo.fn_GetReservationCountByStatus
(
    @status VARCHAR(50)
)
    RETURNS INT
AS
BEGIN
RETURN (
    SELECT COUNT(*)
    FROM dbo.reservations
    WHERE [status] = @status
    );
END;
GO
