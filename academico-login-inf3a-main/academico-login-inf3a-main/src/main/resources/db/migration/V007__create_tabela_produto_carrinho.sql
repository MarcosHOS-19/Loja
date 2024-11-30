IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='produto_carrinho' AND xtype='U')
CREATE TABLE produto_carrinho (
    id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    carrinho_id BIGINT,
    produto_id BIGINT,
    quantidade INT NOT NULL,
    FOREIGN KEY (carrinho_id) REFERENCES carrinhos(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE CASCADE
);