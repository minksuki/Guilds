package me.glaremasters.guilds.database;

import org.intellij.lang.annotations.Language;

/**
 * Created by Glare
 * Date: 4/28/2019
 * Time: 6:05 PM
 */
public class Queries {

    @Language("MySQL")
    public static final String CREATE_GUILDS_TABLE = "CREATE TABLE IF NOT EXISTS `guilds`\n" +
            "(\n" +
            "  `id` varchar(36) UNIQUE PRIMARY KEY NOT NULL,\n" +
            "  `name` varchar(60) UNIQUE NOT NULL,\n" +
            "  `prefix` varchar(60) NOT NULL,\n" +
            "  `master` varchar(36) NOT NULL,\n" +
            " `home` varchar(1000) NULL,\n" +
            "  `skull` varchar(60) NOT NULL,\n" +
            "  `status` varchar(10) NOT NULL,\n" +
            "  `tier` integer(5) NOT NULL,\n" +
            "  `balance` double NOT NULL,\n" +
            " `members` TEXT NOT NULL,\n" +
            " `invited_members` TEXT NOT NULL,\n" +
            " `allies` TEXT NOT NULL,\n" +
            " `pending_allies` TEXT NOT NULL,\n" +
            " `codes` TEXT NOT NULL,\n" +
            " `vaults` TEXT NOT NULL\n" +
            ");";

    @Language("MySQL")
    public static final String ADD_NEW_GUILD = "INSERT IGNORE INTO guilds (id, name, prefix, " +
            "master, home, skull, status, tier, balance, " +
            "members, invited_members, allies, " +
            "pending_allies, codes, vaults) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

}
