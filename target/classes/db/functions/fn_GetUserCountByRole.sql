CREATE FUNCTION dbo.fn_GetUserCountByRole
(
    @role VARCHAR(50)
)
    RETURNS INT
AS
BEGIN
RETURN (
    SELECT COUNT(*)
    FROM dbo.[users]
    WHERE [role] = @role
    );
END;
GO
