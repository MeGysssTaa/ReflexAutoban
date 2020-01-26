/*
 * Copyright 2020 DarksideCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rip.reflex.autoban.util.io;

import rip.reflex.autoban.util.str.Strings;

import java.io.File;
import java.io.FileWriter;

/**
 * Some cool utilities that simplify
 * our life working with files.
 */
public class Files {

    /**
     * Ensure that the path of this file exists by checking
     * all its parent folders and creating them in case they
     * are missing. If the given file is a directory (the
     * name of the file ends with "/"), it is created as well.
     *
     * @param f The file to check.
     */
    public static void ensureExists(final File f) {
        if (f.getName().contains("."))
            // Probably a file
            f.getParentFile().mkdirs();
        else
            // Probably a directory
            f.mkdirs();

//        File parent = f;
//
//        if ((!(f.exists())) && (Strings.replace(f.getName(), "\\", "/").endsWith("/"))) f.mkdirs();
//        while ((parent = parent.getParentFile()) != null)
//            if (!(parent.exists()))
//        parent.mkdirs();
    }

    /**
     * Retreives the file located at the specified path,
     * ensuring it and all its parent folders exist.
     *
     * @param path The path to look at.
     * @return The file located at the given path as with:
     *             new File(path);
     *         (ensuring its parent folders exist).
     */
    public static File getFile(final String path) {
        File f = new File(path);
        ensureExists(f);

        if ((Strings.replace(path, "\\", "/").endsWith("/")) && (!(f.exists()))) // is a directory
            f.mkdirs();
        return f;
    }

    /**
     * Write the given text to the specified file.
     * If file already exists, it will not be overwritten,
     * but will be appended with the given text.
     *
     * @param f The file to write to/append.
     * @param text The text to write/append with.
     */
    public static void write(final File f, final String text) {
        try (final FileWriter fw = new FileWriter(f, true)) {
            fw.write(text + '\n');
            fw.flush();
        } catch (final Exception ex) {
            throw new RuntimeException(String.format("Failed to write %s to '%s'",
                    (text.length() > 50) ? text.length() + " chars" : "'" + text + "'", f.getAbsolutePath()), ex);
        }
    }

}
