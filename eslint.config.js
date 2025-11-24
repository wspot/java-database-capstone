export default [
    {
        files: ["**/*.js"],
        languageOptions: {
            ecmaVersion: "latest",
            sourceType: "module"
        },
        rules: {
            semi: "warn",
            quotes: ["warn", "double"]
        }
    }
];
