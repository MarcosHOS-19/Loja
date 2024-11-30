IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='carrinhos' AND xtype='U')
CREATE TABLE carrinhos (
    id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
    usuario_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);