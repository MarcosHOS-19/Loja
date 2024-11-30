IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='produtos' AND xtype='U')
BEGIN
CREATE TABLE produtos (
                          id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
                          nome VARCHAR(45) NOT NULL,
                          descricao VARCHAR(MAX) NOT NULL,
                           preco FLOAT NOT NULL,
                          quantidade INT NOT NULL
    )
END