IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='papeis' and xtype='U')

create table papeis(
	id BIGINT not null identity(1,1) PRIMARY KEY,
	nome_papel varchar(45) not null,
	descricao_papel varchar(200) null,
	cod_status_papel bit 
)