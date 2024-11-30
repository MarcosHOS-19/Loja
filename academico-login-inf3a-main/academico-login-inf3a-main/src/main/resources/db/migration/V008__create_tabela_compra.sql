IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='compras' AND xtype='U')
CREATE TABLE compras (
    id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    cliente_id BIGINT,  -- Verifique se esta coluna existe
    total DECIMAL(10, 2) NOT NULL,
    data_compra DATETIME NOT NULL,  -- Adicione esta coluna se não existir
    FOREIGN KEY (cliente_id) REFERENCES usuarios(id) ON DELETE CASCADE
);