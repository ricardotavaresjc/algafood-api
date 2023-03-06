#after id estrutura o codigo para ficar logo apos o id
alter table pedido add codigo varchar(36) not null after id;
#atribuir os uuids nos registros existentes
update pedido set codigo = uuid();
#adicionando um indice unico
alter table pedido add constraint  uk_pedido_codigo unique(codigo);