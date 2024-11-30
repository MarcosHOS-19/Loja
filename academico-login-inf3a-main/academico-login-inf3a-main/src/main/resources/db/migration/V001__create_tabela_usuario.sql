IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='usuarios' and xtype='U')

create table usuarios(
id BIGINT not null IDENTITY(1,1) PRIMARY KEY,
nome varchar(45) null,
email varchar(45) not null,
senha varchar(250) not null,
cod_status_usuario bit null,
data_nascimento DATE null,
unique(email)
)