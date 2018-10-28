#!/usr/bin/env tarantool

box.cfg {
    listen = 3301
}


if not box.schema.user.exists('LoweAlpine') then
    box.schema.user.create('LoweAlpine', { password = 'secret' });
    box.schema.user.create('LoweAlpine', 'read, write, execute', 'universe');
end

s = box.space.rotten_cache;
if not s then
    print('********** creating space **********')
    s = box.schema.space.create('rotten_cache', {
        id = 35,
        temporary = false,
        engine = memtx
    });
    print('********** end creating space **********')
    s:create_index('primary_index', {
        --id = 35,
        type = 'HASH',
        parts = { 1, 'string' }
    });
    print('********** end procedure **********')
end