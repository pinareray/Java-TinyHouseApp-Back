CREATE PROCEDURE sp_GetMonthlyReservations
    @year INT,
    @month INT,
    @outCount INT OUTPUT
AS
BEGIN
SELECT @outCount = COUNT(*)
FROM reservations
WHERE YEAR(start_date) = @year AND MONTH(start_date) = @month;
END
