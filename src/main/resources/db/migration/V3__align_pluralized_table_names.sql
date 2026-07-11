DO $$
BEGIN
    IF to_regclass('public.meal_plan_type') IS NOT NULL
       AND to_regclass('public.meal_plan_types') IS NULL THEN
        ALTER TABLE meal_plan_type RENAME TO meal_plan_types;
    END IF;

    IF to_regclass('public.meal_plan_entry') IS NOT NULL
       AND to_regclass('public.meal_plan_entries') IS NULL THEN
        ALTER TABLE meal_plan_entry RENAME TO meal_plan_entries;
    END IF;
END $$;
