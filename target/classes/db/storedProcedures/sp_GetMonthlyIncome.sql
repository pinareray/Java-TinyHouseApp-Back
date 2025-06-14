CREATE PROCEDURE dbo.sp_GetMonthlyIncome
    @year  INT,
  @month INT,
  @outTotal MONEY OUTPUT
AS
BEGIN
SELECT @outTotal = COALESCE(SUM(p.amount), 0)
FROM dbo.payments p
WHERE YEAR(p.payment_date)  = @year
  AND MONTH(p.payment_date) = @month;
END;
GO
