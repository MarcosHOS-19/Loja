IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='usuarios_papeis' and xtype='U')

create table usuarios_papeis(
usuario_id BIGINT NULL,
papel_id BIGINT NULL,
CONSTRAINT fk_usuarios_papeis_usuario_id FOREIGN KEY(usuario_id) references usuarios(id),
CONSTRAINT fk_usuarios_papeis_papel_id FOREIGN KEY(papel_id) references papeis(id),
)